import { createClient } from "https://cdn.jsdelivr.net/npm/@supabase/supabase-js/+esm";


const SUPABASE_URL = 'https://zckpocoedkulokcpxvqu.supabase.co';
const SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inpja3BvY29lZGt1bG9rY3B4dnF1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQwNzE4ODMsImV4cCI6MjA2OTY0Nzg4M30.3HdOMxvIqSnkPFxpUAOrCLXIba3G9Wo95VMxSE_BqUs';

export const supabase = createClient(SUPABASE_URL, SUPABASE_ANON_KEY);
