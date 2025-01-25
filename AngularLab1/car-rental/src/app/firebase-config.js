import { initializeApp } from "firebase/app";

const firebaseConfig = { 
  apiKey: "AIzaSyBvJCYiXVgKXAdWBXBf_LjvlYoD2GIEtOI", 
  authDomain: "newproject-6dba8.firebaseapp.com", 
  projectId: "newproject-6dba8", 
  storageBucket: "newproject-6dba8.appspot.com", 
  messagingSenderId: "997730581161", 
  appId: "1:997730581161:web:d14c2a4a5162a6019087b8" 
};

// Инициализация Firebase
const app = initializeApp(firebaseConfig);

export { app }; 