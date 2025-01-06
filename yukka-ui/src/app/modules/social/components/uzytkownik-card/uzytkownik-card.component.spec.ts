import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UzytkownikCardComponent } from './uzytkownik-card.component';

describe('UzytkownikCardComponent', () => {
  let component: UzytkownikCardComponent;
  let fixture: ComponentFixture<UzytkownikCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UzytkownikCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UzytkownikCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
