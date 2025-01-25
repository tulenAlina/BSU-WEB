import { Component, OnInit } from '@angular/core';
import { CarService } from '../services/car.service';
import { Router } from '@angular/router';
import { Car } from '../car.model'; // Импортируйте модель Car
import { Observable, of } from 'rxjs';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
})
export class CarListComponent implements OnInit {
  cars$: Observable<Car[]> = of([]); // Изначально пустой Observable для автомобилей
  loading: boolean = true; // Флаг загрузки
  error: string | null = null; // Переменная для хранения ошибки

  constructor(private carService: CarService, private router: Router) {}

  ngOnInit(): void {
    this.loadCars(); // Загрузка автомобилей при инициализации
  }

  loadCars() {
    this.cars$ = this.carService.getCars(); // Получение автомобилей из сервиса
    this.cars$.subscribe({
      next: () => {
        this.loading = false; // Сбрасываем флаг загрузки
      },
      error: (err) => {
        this.error = 'Не удалось загрузить автомобили.'; // Обработка ошибки
        console.error(err); // Логирование ошибки в консоль
        this.loading = false; // Сбрасываем флаг загрузки
      }
    });
  }

  viewDetails(carId: number) {
    this.router.navigate(['/details', carId]); // Навигация к деталям автомобиля
  }
}