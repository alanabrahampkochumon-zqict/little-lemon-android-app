import supabase from "./supabase.js";

supabase.auth
    .signInWithOtp({ email: "alanabrahampkochumon.zqict@gmail.com" })
    .then((res) => console.log(res))
    .catch((e) => console.log(e));
