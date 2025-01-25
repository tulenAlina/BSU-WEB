import { Injectable } from '@angular/core';
import { Firestore, collection, collectionData, getDocs, addDoc, setDoc, doc, query, where, orderBy, deleteDoc } from '@angular/fire/firestore';
import { Observable, from } from 'rxjs';
import { map } from 'rxjs/operators'; // Импортируем map оператор
import { Car } from '../car.model';

@Injectable({
  providedIn: 'root'
})
export class CarService {
  private dbPath = '/car-list'; // Путь к коллекции в Firestore

  constructor(private db: Firestore) {}

  // Получение списка автомобилей как Observable
  getCars(): Observable<Car[]> {
    const q = query(collection(this.db, this.dbPath), orderBy('id'));
    return from(getDocs(q)).pipe(
      map(querySnapshot => querySnapshot.docs.map(doc => ({
        id: doc.data()['id'],
        model: doc.data()['model'],
        price: doc.data()['price'],
        state: doc.data()['state']
      } as Car)))
    );
  }

  // Добавление нового автомобиля
  async addCar(car: Car): Promise<void> {
    await addDoc(collection(this.db, this.dbPath), { ...car });
  }

  // Обновление автомобиля по id
  async updateCar(updatedCar: Car): Promise<void> {
    const q = query(collection(this.db, this.dbPath), where('id', '==', updatedCar.id));
    const querySnapshot = await getDocs(q);
    querySnapshot.forEach(async (d) => {
      const docRef = doc(this.db, this.dbPath, d.id);
      await setDoc(docRef, { model: updatedCar.model, state: updatedCar.state, price: updatedCar.price }, { merge: true });
    });
  }

  // Удаление автомобиля по id
  async deleteCar(carId: number): Promise<void> {
    const q = query(collection(this.db, this.dbPath), where('id', '==', carId));
    const querySnapshot = await getDocs(q);
    querySnapshot.forEach(async (docElement) => {
      await deleteDoc(doc(this.db, this.dbPath, docElement.id));
    });
  }
}