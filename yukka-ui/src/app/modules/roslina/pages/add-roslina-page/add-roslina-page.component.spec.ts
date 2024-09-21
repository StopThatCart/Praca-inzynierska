import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRoslinaPageComponent } from './add-roslina-page.component';

describe('AddRoslinaPageComponent', () => {
  let component: AddRoslinaPageComponent;
  let fixture: ComponentFixture<AddRoslinaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddRoslinaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRoslinaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
