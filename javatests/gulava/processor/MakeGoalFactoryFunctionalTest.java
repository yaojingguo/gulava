/*
 *  Copyright (c) 2015 The Gulava Authors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package gulava.processor;

import gulava.Cons;
import gulava.Goal;
import gulava.Goals;
import gulava.annotation.MakeGoalFactory;
import gulava.testing.LogicAsserter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class MakeGoalFactoryFunctionalTest {
  @MakeGoalFactory(name = "HasAnAtom")
  public static class HasAnAtomClauses {
    static Goal found(Cons<HasNoFields, ?> a) {
      return Goals.UNIT;
    }

    static Goal iterate(Cons<?, ?> a) {
      return HasAnAtom.o(a.cdr());
    }
  }

  @Test
  public void decomposeEmptyField() {
    new LogicAsserter()
        .stream(HasAnAtom.o(Cons.list(Arrays.asList(1, 2))))
        .workUnits(0)
        .test();

    new LogicAsserter()
        .stream(HasAnAtom.o(Cons.list(Arrays.asList(1, 2, HasNoFields.of(), 3, 4))))
        .workUnits(1)
        .startSubst()
        .test();
  }
}
