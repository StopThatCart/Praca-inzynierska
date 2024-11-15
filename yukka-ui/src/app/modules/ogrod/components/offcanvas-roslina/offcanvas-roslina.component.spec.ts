import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OffcanvasRoslinaComponent } from './offcanvas-roslina.component';

describe('OffcanvasRoslinaComponent', () => {
  let component: OffcanvasRoslinaComponent;
  let fixture: ComponentFixture<OffcanvasRoslinaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OffcanvasRoslinaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OffcanvasRoslinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
