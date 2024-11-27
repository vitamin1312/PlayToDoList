package bootstrap

import com.google.inject.AbstractModule

class TasksDatabaseModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[InitialData]).asEagerSingleton()
  }
}