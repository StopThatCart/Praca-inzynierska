import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalNotatkaPickComponent } from './modal-notatka-pick.component';

describe('ModalNotatkaPickComponent', () => {
  let component: ModalNotatkaPickComponent;
  let fixture: ComponentFixture<ModalNotatkaPickComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalNotatkaPickComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalNotatkaPickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
