import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WysokoscInputComponent } from './wysokosc-input.component';

describe('WysokoscInputComponent', () => {
  let component: WysokoscInputComponent;
  let fixture: ComponentFixture<WysokoscInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WysokoscInputComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WysokoscInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
