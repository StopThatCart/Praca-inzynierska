import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaWlasciwosciContainerComponent } from './roslina-wlasciwosci-container.component';

describe('RoslinaWlasciwosciContainerComponent', () => {
  let component: RoslinaWlasciwosciContainerComponent;
  let fixture: ComponentFixture<RoslinaWlasciwosciContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaWlasciwosciContainerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaWlasciwosciContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
