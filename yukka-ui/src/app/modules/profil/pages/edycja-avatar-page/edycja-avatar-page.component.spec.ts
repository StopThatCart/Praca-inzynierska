import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdycjaAvatarPageComponent } from './edycja-avatar-page.component';

describe('EdycjaAvatarPageComponent', () => {
  let component: EdycjaAvatarPageComponent;
  let fixture: ComponentFixture<EdycjaAvatarPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EdycjaAvatarPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EdycjaAvatarPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
