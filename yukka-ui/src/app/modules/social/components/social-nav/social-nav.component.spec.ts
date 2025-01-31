import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SocialNavComponent } from './social-nav.component';

describe('SocialNavComponent', () => {
  let component: SocialNavComponent;
  let fixture: ComponentFixture<SocialNavComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SocialNavComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SocialNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
