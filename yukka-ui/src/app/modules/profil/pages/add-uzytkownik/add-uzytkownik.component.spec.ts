import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUzytkownikComponent } from './add-uzytkownik.component';

describe('AddUzytkownikComponent', () => {
  let component: AddUzytkownikComponent;
  let fixture: ComponentFixture<AddUzytkownikComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddUzytkownikComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddUzytkownikComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
