class TLocalStorage {
    constructor() {
      this.storage = window.localStorage;
    }
  
    Reset() {
      this.storage.clear();
    }
  
    AddValue(key, value) {
      if (key && value) {
        this.storage.setItem(key, JSON.stringify(value));
      }
    }
  
    GetValue(key) {
      const value = this.storage.getItem(key);
      if (value) {
        return JSON.parse(value);
      } else {
        return "Нет информации";
      }
    }
  
    DeleteValue(key) {
      this.storage.removeItem(key);
    }
  
    GetKeys() {
      return Object.keys(this.storage);
    }
  }
  
const localStorage = new TLocalStorage();

class THashStorage {
    constructor() {
    this.storage = {};
    }
    
    Reset() {
        this.storage = {};
    }
    
    AddValue(key, value) {
        if(key && value) {
            this.storage[key] = value;
        }
    }
    
    GetValue(key) {
        if (this.storage.hasOwnProperty(key)) {
            return this.storage[key];
        } else {
            return "Нет информации";
        }
    }
    
    DeleteValue(key) {
        delete this.storage[key];
    }
    
    GetKeys() {
        let result = "";
        for (let key in this.storage) {
            result += `${key}\n`;
        }
    return result;
  }
}

const storage = new THashStorage();

function addPolicy() {
    const key = prompt("Введите название полиса:");
    const value = prompt("Введите стоимость полиса:");
    storage.AddValue(key, value);
    localStorage.AddValue(key, value); // Добавляем также в локальное хранилище
  }
  
  function deletePolicy() {
    const key = prompt("Введите название полиса:");
    storage.DeleteValue(key);
    localStorage.DeleteValue(key); // Удаляем также из локального хранилища
  }
  
  function getPolicyInfo() {
    const key = prompt("Введите название полиса:");
    if (key) {
      const info = storage.GetValue(key);
      const localInfo = localStorage.GetValue(key); // Получаем информацию из локального хранилища
      console.log(localInfo);
    }
  }
  
  function listPolicies() {
    const policiesList = storage.GetKeys();
    const localPoliciesList = localStorage.GetKeys(); // Получаем список ключей из локального хранилища
  
    if (localPoliciesList.length === 0) {
      console.log("Нет элементов");
    }
    else {
        for (key of localPoliciesList) {
        console.log(key);
        }
    }
}