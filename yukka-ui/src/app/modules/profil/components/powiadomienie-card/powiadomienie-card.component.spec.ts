import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PowiadomienieCardComponent } from './powiadomienie-card.component';

describe('PowiadomienieCardComponent', () => {
  let component: PowiadomienieCardComponent;
  let fixture: ComponentFixture<PowiadomienieCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PowiadomienieCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PowiadomienieCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
