import { fromEvent, switchMap, map } from 'rxjs';
import { ajax } from "rxjs/ajax";

const showDataButton = document.getElementById('show-data-button');
const deleteButton = document.getElementById('delete-button');
const dataContainer = document.getElementById('data-container');

// Обработка события "Показать данные"
const showData$ = fromEvent(showDataButton, 'click').pipe(
  switchMap(() => ajax.getJSON('../data.json')),
  map(data => Object.entries(data)),
);

showData$.subscribe(dataEntries => {
  dataContainer.innerHTML = '';
  dataEntries.forEach(([key, value]) => {
    const row = loadVisitor(key, value);
    dataContainer.appendChild(row);
  });
});

const loadVisitor = (login, name) => {
  const row = document.createElement('tr');
  const loginCell = document.createElement('td');
  loginCell.textContent = login;
  const nameCell = document.createElement('td');
  nameCell.textContent = name;
  row.appendChild(loginCell);
  row.appendChild(nameCell);
  return row;
}
// Обработка события "Удалить"
fromEvent(deleteButton, 'click')
  .subscribe(() => {
    // Удаление последней строки с данными
    const rows = dataContainer.innerText.split('\n');
    rows.pop();
    dataContainer.innerText = rows.join('\n');
  });