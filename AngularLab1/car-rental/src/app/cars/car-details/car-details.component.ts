import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Cars } from '../mock-car-list';

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
})
export class CarDetailsComponent implements OnInit {
  car: any;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.car = Cars.find(car => car.id === id);
  }
}