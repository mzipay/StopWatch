# StopWatch

StopWatch is a general-purpose productivity tool that can track the time an
individual spends on work tasks and interruptions.

A "task" is loosely defined as any activity related to work that an individual
must perform.  Those activities that are considered work tasks will vary from
individual to individual; StopWatch enforces no rules to differentiate between
what is or is not a "task."  For example, "Responding to emails" may be
considered a work task for a customer service representative, but not
necessarily for a programmer.

An "interruption" is any activity that causes an individual's attention to be
diverted from a task in which s/he is currently engaged.  Note that a task may,
under certain circumstances, also be considered an interruption.  For example,
"Troubleshoot issue log entry #171923" might be a work task that an individual
engages in, but it would qualify as an interruption if that individual were
already engaged in another task when an emergency request came in to
"Troubleshoot issue log entry #171923."

The important thing to remember is that StopWatch is meant not only to track
how an individual spends his/her time, but also to measure the frequency with
and degree to which the individual's attention is diverted from work tasks or
to additional work tasks.  This is an important measurement for an individual
to both recognize and communicate, as numerous studies have shown that
"context switching" (or "multitasking") can have a degrading effect on an
individual's productivity [Dzubak 2007, Czerwinski et al 2004, Rubenstein
et al 2001].

## Installing

```shell
$ git clone https://github.com/mzipay/StopWatch.git
$ cd StopWatch
$ make
$ make run
```

Subsequent runs can just use ``make run``.

## How to use

1. Click the "Begin task" button when you engage in a work task.  In the
   subsequent popup window, supply a short description of the task that will
   be meaningful to you when reviewing your data in the future.  The task
   timer in the central area of the StopWatch window will begin counting.
   While engaged in a task that is not being interrupted, the counter will
   appear in a green font.
2. If interrupted while engaged in a work task, toggle the "Interruption"
   button.  The interruption timer in the lower right area of the StopWatch
   window will begin counting (note that the task timer continues to tick as
   well).  While engaged in an interruption, the task timer appears in a
   yellow font, and the interruption timer in a red font.
3. When an interruption has ended, untoggle the "Interruption" button.  In the
   subsequent popup window, supply a short description of the interruption
   that will be meaningful to you when reviewing your data in the future.
   The interruption timer will reset to zero and will be dimmed.  The task
   timer will return to its green font color. 
4. When finished working on a task, click the "End task" button.  The task
   timer will reset to zero and will be dimmed.  The task and all
   interruptions will be saved to the StopWatch database, and can be viewed
   and/or exported to a file by choosing "View database..." or
   "Export database...", respectively, from the "File" menu.

Viewing the StopWatch database:

To view the database, choose "View database..." from the "File" menu.

When viewing the StopWatch database, the following fields are shown:

For tasks:
    id - a unique, internal identifier for the task
    description - the task description provided by the user
    start_time - the date/time marking when the user began working on the task
    end_time - the date/time marking when the user finished working on the task

For interruptions:
    id - a unique, internal identifier for the interruption
    related_task_id - identifies the task that was interrupted
    description - the interruption description provided by the user
    start_time - the date/time marking when the user was interrupted
    end_time - the date/time marking when the interruption was finished

## Disclaimer

StopWatch is not, and does not claim to be, an application that can provide any
scientifically meaningful data.  It is a simple tool whose aim is merely to
allow an individual to become aware of how his/her time is spent.

