import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WyswietlanieRoslinyOpcjeComponent } from './wyswietlanie-rosliny-opcje.component';

describe('WyswietlanieRoslinyOpcjeComponent', () => {
  let component: WyswietlanieRoslinyOpcjeComponent;
  let fixture: ComponentFixture<WyswietlanieRoslinyOpcjeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WyswietlanieRoslinyOpcjeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WyswietlanieRoslinyOpcjeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
