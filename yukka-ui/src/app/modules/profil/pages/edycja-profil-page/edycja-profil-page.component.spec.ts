import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdycjaProfilPageComponent } from './edycja-profil-page.component';

describe('EdycjaProfilPageComponent', () => {
  let component: EdycjaProfilPageComponent;
  let fixture: ComponentFixture<EdycjaProfilPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EdycjaProfilPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EdycjaProfilPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
