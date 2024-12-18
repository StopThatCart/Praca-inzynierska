import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPowiadomienieModalComponent } from './add-powiadomienie-modal.component';

describe('AddPowiadomienieModalComponent', () => {
  let component: AddPowiadomienieModalComponent;
  let fixture: ComponentFixture<AddPowiadomienieModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddPowiadomienieModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddPowiadomienieModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
