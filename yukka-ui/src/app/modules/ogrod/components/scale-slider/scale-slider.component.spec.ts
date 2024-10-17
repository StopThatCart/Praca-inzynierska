import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScaleSliderComponent } from './scale-slider.component';

describe('ScaleSliderComponent', () => {
  let component: ScaleSliderComponent;
  let fixture: ComponentFixture<ScaleSliderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScaleSliderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScaleSliderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
