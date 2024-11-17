import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdycjaEmailPageComponent } from './edycja-email-page.component';

describe('EdycjaEmailPageComponent', () => {
  let component: EdycjaEmailPageComponent;
  let fixture: ComponentFixture<EdycjaEmailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EdycjaEmailPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EdycjaEmailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
