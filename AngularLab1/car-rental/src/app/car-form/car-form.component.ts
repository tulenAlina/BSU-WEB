import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { CarService } from '../cars/services/car.service'; // Импортируйте сервис
import { Car } from '../cars/car.model';
import { Observable, of, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-car-form',
  templateUrl: './car-form.component.html',
})
export class CarFormComponent implements OnInit {
  cars$: Observable<Car[]> = of([]); // Инициализируем как пустой Observable
  selectedCar: Car | null = null;
  activeEditButtonId: number | null = null;
  loading: boolean = true; // Флаг загрузки
  error: string | null = null; // Переменная для хранения ошибки

  constructor(private carService: CarService) {}

  ngOnInit(): void {
    this.loadCars(); // Загружаем автомобили при инициализации
  }

  loadCars() {
    this.cars$ = this.carService.getCars(); // Получаем список из сервиса
    this.cars$.subscribe({
      next: () => {
        this.loading = false; // Сбрасываем флаг загрузки
      },
      error: (err) => {
        this.error = 'Не удалось загрузить автомобили.'; // Обработка ошибки
        console.error(err); // Логируем ошибку
        this.loading = false; // Сбрасываем флаг загрузки
      },
    });
  }

  async onSubmit(form: NgForm) {
    if (form.invalid) {
      return; // Если форма не валидна, ничего не делаем
    }
  
    const cars = await this.carService.getCars().toPromise(); // Получаем список автомобилей
  
    if (!cars) {
      // Обработка случая, когда cars undefined
      return; // Или можно инициализировать cars как пустой массив
    }
  
    if (this.selectedCar) {
      // Обновление существующего автомобиля
      await this.carService.updateCar({ id: this.selectedCar.id, ...form.value });
    } else {
      // Добавление нового автомобиля
      const newId = cars.length > 0 ? Math.max(...cars.map(car => car.id)) + 1 : 1;
      await this.carService.addCar({ id: newId, ...form.value });
    }
  
    this.loadCars(); // Обновляем список автомобилей после добавления или обновления
    form.reset();
    this.selectedCar = null; // Сброс выбранного автомобиля
  }

  async onDelete(car: Car) {
    await this.carService.deleteCar(car.id); // Удаляем автомобиль через сервис
    this.loadCars(); // Обновляем список автомобилей
    if (this.activeEditButtonId === car.id) {
      this.activeEditButtonId = null; // Сбрасываем активный ID
    }
  }

  onEdit(car: Car) {
    if (this.activeEditButtonId === car.id) {
      this.activeEditButtonId = null; // Если кнопка уже активна, сбрасываем выделение
      this.selectedCar = null; // Сброс выбранного автомобиля
    } else {
      this.selectedCar = { ...car }; // Устанавливаем выделение для новой кнопки
      this.activeEditButtonId = car.id;
    }
  }
}