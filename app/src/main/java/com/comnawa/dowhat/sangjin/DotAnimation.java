package com.comnawa.dowhat.sangjin;

import android.view.animation.AlphaAnimation;

import static android.R.attr.fromAlpha;

/**
 * Created by sangjin on 2017. 6. 29..
 */

public class DotAnimation {

  public AnimationAlphaOnt alphaOn(float formAlpha, float toAlpha){
    return new AnimationAlphaOnt(formAlpha, toAlpha);
  }

  public AnimationAlphaOfft alphaOut(float formAlpha, float toAlpha){
    return new AnimationAlphaOfft(fromAlpha, toAlpha);
  }

  class AnimationAlphaOnt extends AlphaAnimation {
    public AnimationAlphaOnt(float fromAlpha, float toAlpha) {
      super(fromAlpha, toAlpha);
      setDuration(100);
    }
  }

  class AnimationAlphaOfft extends AlphaAnimation {
    public AnimationAlphaOfft(float fromAlpha, float toAlpha) {
      super(fromAlpha, toAlpha);
      setDuration(2000);
    }
  }



}
