import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCustomCechaComponent } from './add-custom-cecha.component';

describe('AddCustomCechaComponent', () => {
  let component: AddCustomCechaComponent;
  let fixture: ComponentFixture<AddCustomCechaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddCustomCechaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCustomCechaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
