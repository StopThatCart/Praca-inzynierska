import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaPageComponent } from './roslina-page.component';

describe('RoslinaPageComponent', () => {
  let component: RoslinaPageComponent;
  let fixture: ComponentFixture<RoslinaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
