import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlokButtonComponent } from './blok-button.component';

describe('BlokButtonComponent', () => {
  let component: BlokButtonComponent;
  let fixture: ComponentFixture<BlokButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlokButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BlokButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
