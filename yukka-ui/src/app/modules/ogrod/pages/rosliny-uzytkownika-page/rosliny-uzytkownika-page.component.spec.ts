import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinyUzytkownikaPageComponent } from './rosliny-uzytkownika-page.component';

describe('RoslinyUzytkownikaPageComponent', () => {
  let component: RoslinyUzytkownikaPageComponent;
  let fixture: ComponentFixture<RoslinyUzytkownikaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinyUzytkownikaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinyUzytkownikaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
