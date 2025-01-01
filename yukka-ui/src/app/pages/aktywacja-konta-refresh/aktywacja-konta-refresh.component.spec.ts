import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AktywacjaKontaRefreshComponent } from './aktywacja-konta-refresh.component';

describe('AktywacjaKontaRefreshComponent', () => {
  let component: AktywacjaKontaRefreshComponent;
  let fixture: ComponentFixture<AktywacjaKontaRefreshComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AktywacjaKontaRefreshComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AktywacjaKontaRefreshComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
