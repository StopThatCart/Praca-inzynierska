import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRoslinaToDzialkaComponent } from './add-roslina-to-dzialka.component';

describe('AddRoslinaToDzialkaComponent', () => {
  let component: AddRoslinaToDzialkaComponent;
  let fixture: ComponentFixture<AddRoslinaToDzialkaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRoslinaToDzialkaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRoslinaToDzialkaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
