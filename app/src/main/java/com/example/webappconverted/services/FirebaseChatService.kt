package com.example.webappconverted.services

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// FirebaseChatService implements core chat functions: sendMessage, listenForMessages, presence and typing.
// Original JS: chat.js - functions: updatePresence, typing status, onChildAdded listeners, onValue presence listeners.
class FirebaseChatService(private val database: FirebaseDatabase, private val currentUser: String, private val partner: String) {
    private val TAG = "FirebaseChatService"
    private val chatId = listOf(currentUser, partner).sorted().joinToString("_")
    private val chatRef = database.getReference("chats").child(chatId)
    private val typingRef = database.getReference("typingStatus")
    private val presenceRef = database.getReference("presence")

    suspend fun sendMessage(content: String) {
        val msgRef = chatRef.push()
        val map = mapOf(
            "sender" to currentUser,
            "receiver" to partner,
            "content" to content,
            "timestamp" to System.currentTimeMillis(),
            "status" to "sent"
        )
        msgRef.setValue(map).await()
    }

    fun observeMessages() = callbackFlow {
        val listener = object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                trySend(snapshot)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { trySend(snapshot) }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        chatRef.addChildEventListener(listener)
        awaitClose { chatRef.removeEventListener(listener) }
    }

    fun setTyping(isTyping: Boolean) {
        typingRef.child(currentUser).setValue(isTyping)
    }

    fun observePartnerTyping(callback: (Boolean)->Unit) {
        typingRef.child(partner).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val v = snapshot.getValue(Boolean::class.java) ?: false
                callback(v)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun updatePresence(online: Boolean) {
        presenceRef.child(currentUser).setValue(mapOf("online" to online, "lastSeen" to System.currentTimeMillis(), "onChat" to true))
    }

    fun observePartnerPresence(callback: (Map<String, Any?>)->Unit) {
        presenceRef.child(partner).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = snapshot.value as? Map<String, Any?>
                if (map != null) callback(map)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Mark message as read/delivered
    fun updateMessageStatus(messageKey: String, status: String) {
        chatRef.child(messageKey).child("status").setValue(status)
    }
}
