import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoslinaListComponent } from './roslina-list.component';

describe('RoslinaListComponent', () => {
  let component: RoslinaListComponent;
  let fixture: ComponentFixture<RoslinaListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoslinaListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoslinaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
