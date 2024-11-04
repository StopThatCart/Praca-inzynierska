import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalColorPickComponent } from './modal-color-pick.component';

describe('ModalColorPickComponent', () => {
  let component: ModalColorPickComponent;
  let fixture: ComponentFixture<ModalColorPickComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalColorPickComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalColorPickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
