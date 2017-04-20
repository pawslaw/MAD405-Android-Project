# MAD405-Android-Project
## Git Config Instructions
Set name: `git config --global user.name` `<<<YOUR NAME HERE>>>`

Set email: `git config --global user.email` `<<<YOUR EMAIL HERE>>>`

## Commiting Instructions
First, we'll make sure we're up to date with the *staging* branch using the following command:
`git pull` `staging`

We will then change branch to *staging*. To change from our current branch to staging, we execute the following:
`git checkout` `staging`

Create your feature branch, the feature branch name should be descriptive of the feature you are adding to the project. To do this, run:
`git checkout -b` `<<<YOUR BRANCH NAME>>>`

Next, you will push the branch to the git repository.
`git push origin` `<<<YOUR BRANCH NAME>>>`

Make the changes to the application, making commits as necessary (`git commit -m` `<<<COMMIT MESSAGE>>>`). Make sure you write *JavaDoc* comments as necessary. **I will not merge your branches if you do not have this.**
When complete the feature, merge the *staging* branch into *your branch*. You will probably end up with a merge conflict. Resolve the conflict. **Ensure the application compiles**. Make a commit after you resolve the merge conflict.

Finially, create a *pull request* for *your branch* to the *staging branch*. Make sure you watch the pull request page for my feedback.