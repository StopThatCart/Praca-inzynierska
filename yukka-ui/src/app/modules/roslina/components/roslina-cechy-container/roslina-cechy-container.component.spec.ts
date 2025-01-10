import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaCechyContainerComponent } from './roslina-cechy-container.component';

describe('RoslinaCechyContainerComponent', () => {
  let component: RoslinaCechyContainerComponent;
  let fixture: ComponentFixture<RoslinaCechyContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaCechyContainerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaCechyContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
