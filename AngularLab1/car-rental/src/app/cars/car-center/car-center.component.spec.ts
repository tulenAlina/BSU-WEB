import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarCenterComponent } from './car-center.component';

describe('CarCenterComponent', () => {
  let component: CarCenterComponent;
  let fixture: ComponentFixture<CarCenterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CarCenterComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CarCenterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
