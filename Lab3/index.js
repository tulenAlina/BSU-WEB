const policies = {};

function addValue(key, value) {
    if (key && value)
        policies[key] = value;
}

function deleteValue(key) {
    delete policies[key];
}

function getInfo(key) {
    if (policies.hasOwnProperty(key)) {
        return policies[key];
    } else if (key != null) {
        return "Нет информации";
    }
    else {
        return "Название полиса не было введено"
    }
}

function listP() {
    let result = "";
    for (let key in policies) {
        result += `${key}: ${policies[key]}\n`;
    }
    return result;
}

function addPolicy() {
    const key = prompt("Введите название полиса:");
    const value = prompt("Введите стоимость полиса:");
    addValue(key, value);
}

function deletePolicy() {
    const key = prompt("Введите название полиса:");
    deleteValue(key);
}

function getPolicyInfo() {
    const key = prompt("Введите название полиса:");
    const info = getInfo(key);
    console.log(info);
}

function listPolicies() {
    const policiesList = listP();
    console.log(policiesList);
    

    if (policiesList == "")
    {
        console.log("Нет элементов");
    }
}