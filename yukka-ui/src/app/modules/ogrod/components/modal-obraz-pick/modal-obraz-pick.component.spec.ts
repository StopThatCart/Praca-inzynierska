import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalObrazPickComponent } from './modal-obraz-pick.component';

describe('ModalObrazPickComponent', () => {
  let component: ModalObrazPickComponent;
  let fixture: ComponentFixture<ModalObrazPickComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalObrazPickComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalObrazPickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
