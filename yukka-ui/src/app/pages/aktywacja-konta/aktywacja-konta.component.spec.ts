import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AktywacjaKontaComponent } from './aktywacja-konta.component';

describe('AktywacjaKontaComponent', () => {
  let component: AktywacjaKontaComponent;
  let fixture: ComponentFixture<AktywacjaKontaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AktywacjaKontaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AktywacjaKontaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
