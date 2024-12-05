import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdycjaUsunKontoPageComponent } from './edycja-usun-konto-page.component';

describe('EdycjaUsunKontoPageComponent', () => {
  let component: EdycjaUsunKontoPageComponent;
  let fixture: ComponentFixture<EdycjaUsunKontoPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EdycjaUsunKontoPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EdycjaUsunKontoPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
