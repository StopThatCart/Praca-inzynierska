import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EdycjaNavComponent } from './edycja-nav.component';

describe('EdycjaNavComponent', () => {
  let component: EdycjaNavComponent;
  let fixture: ComponentFixture<EdycjaNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EdycjaNavComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EdycjaNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
