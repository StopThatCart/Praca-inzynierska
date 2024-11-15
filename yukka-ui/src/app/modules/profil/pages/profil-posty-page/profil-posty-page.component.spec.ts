import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilPostyPageComponent } from './profil-posty-page.component';

describe('ProfilPostyPageComponent', () => {
  let component: ProfilPostyPageComponent;
  let fixture: ComponentFixture<ProfilPostyPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfilPostyPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilPostyPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
