/*
 *  Copyright (c) 2016 The Gulava Authors
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
package gulava;

import java.io.IOException;

/**
 * A goal which is comprised of subgoals.
 */
public abstract class CompositeGoal implements Goal, Dumpable {
  private final String dumpHeading;
  final Goal[] allGoals;

  CompositeGoal(String dumpHeading, Goal g1, Goal g2, Goal[] gs) {
    allGoals = new Goal[gs.length + 2];
    int i = 0;
    allGoals[0] = g1;
    allGoals[1] = g2;
    System.arraycopy(gs, 0, allGoals, 2, gs.length);

    this.dumpHeading = dumpHeading;
  }

  @Override
  public void dump(Dumper dumper) throws IOException {
    dumper.dump(dumpHeading, (Object[]) allGoals);
  }
}