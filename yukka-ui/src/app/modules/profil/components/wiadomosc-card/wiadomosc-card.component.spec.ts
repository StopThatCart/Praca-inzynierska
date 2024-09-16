import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WiadomoscCardComponent } from './wiadomosc-card.component';

describe('WiadomoscCardComponent', () => {
  let component: WiadomoscCardComponent;
  let fixture: ComponentFixture<WiadomoscCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WiadomoscCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WiadomoscCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
