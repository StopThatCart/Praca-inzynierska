import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalWyswietlanieRoslinyPickComponent } from './modal-wyswietlanie-rosliny-pick.component';

describe('ModalWyswietlanieRoslinyPickComponent', () => {
  let component: ModalWyswietlanieRoslinyPickComponent;
  let fixture: ComponentFixture<ModalWyswietlanieRoslinyPickComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalWyswietlanieRoslinyPickComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalWyswietlanieRoslinyPickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
