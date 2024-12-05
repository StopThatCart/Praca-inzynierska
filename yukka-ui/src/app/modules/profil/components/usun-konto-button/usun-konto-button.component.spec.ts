import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsunKontoButtonComponent } from './usun-konto-button.component';

describe('UsunKontoButtonComponent', () => {
  let component: UsunKontoButtonComponent;
  let fixture: ComponentFixture<UsunKontoButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsunKontoButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsunKontoButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
