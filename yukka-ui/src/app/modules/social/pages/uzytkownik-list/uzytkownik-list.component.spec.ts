import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UzytkownikListComponent } from './uzytkownik-list.component';

describe('UzytkownikListComponent', () => {
  let component: UzytkownikListComponent;
  let fixture: ComponentFixture<UzytkownikListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UzytkownikListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UzytkownikListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
