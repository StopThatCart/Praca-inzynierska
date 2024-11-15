import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilKomentarzePageComponent } from './profil-komentarze-page.component';

describe('ProfilKomentarzePageComponent', () => {
  let component: ProfilKomentarzePageComponent;
  let fixture: ComponentFixture<ProfilKomentarzePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfilKomentarzePageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilKomentarzePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
