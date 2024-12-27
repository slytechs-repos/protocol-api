package com.slytechs.jnet.protocol.tcpipREFACTOR.ip;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.slytechs.jnet.platform.api.util.Registration;

/**
 * Manages event dispatching for IP fragment reassembly operations. Optimizes
 * for cases where no listeners are registered by using a no-op implementation.
 */
public class IpReassemblyEventManager {

	/**
	 * No-op implementation used when no listeners are registered.
	 */
	private static class NoopEventManager extends IpReassemblyEventManager {
		@Override
		public Registration addEventListener(IpReassemblyEventListener listener) {
			// Switch to active manager when first listener is added
			IpReassemblyEventManager activeManager = new IpReassemblyEventManager(true);
			return activeManager.addEventListener(listener);
		}

		@Override
		public void dispatchEvent(IpReassemblyEvent event) {
			// No-op as there are no listeners
		}

		@Override
		public boolean hasListeners() {
			return false;
		}

		/**
		 * 
		 */
		public NoopEventManager() {
			super(false);
		}
	}

	/**
	 * Factory method to create an appropriate event manager instance.
	 *
	 * @param isEventDispatchEnabled whether event dispatch is enabled in config
	 * @return an event manager instance
	 */
	public static IpReassemblyEventManager create(boolean isEventDispatchEnabled) {
		return isEventDispatchEnabled ? new IpReassemblyEventManager(true) : new NoopEventManager();
	}

	// List of registered event listeners
	private final List<IpReassemblyEventListener> listeners;

	// Flag to track if we're currently dispatching events
	private boolean isDispatching;

	// Lists for pending listener modifications during dispatch
	private List<IpReassemblyEventListener> pendingAdditions;
	private List<IpReassemblyEventListener> pendingRemovals;

	/**
	 * Private constructor used by factory method.
	 *
	 * @param useThreadSafeList whether to use a thread-safe list implementation
	 */
	private IpReassemblyEventManager(boolean useThreadSafeList) {
		this.listeners = useThreadSafeList ? new CopyOnWriteArrayList<>() : new ArrayList<>();
		this.isDispatching = false;
	}

	/**
	 * Adds an event listener and returns a registration that can be used to remove
	 * it.
	 *
	 * @param listener the listener to add
	 * @return a registration object for removing the listener
	 * @throws NullPointerException if listener is null
	 */
	public Registration addEventListener(IpReassemblyEventListener listener) {
		if (listener == null) {
			throw new NullPointerException("Event listener cannot be null");
		}

		if (isDispatching) {
			if (pendingAdditions == null) {
				pendingAdditions = new ArrayList<>();
			}
			pendingAdditions.add(listener);
		} else {
			listeners.add(listener);
		}

		return () -> removeEventListener(listener);
	}

	/**
	 * Removes an event listener.
	 *
	 * @param listener the listener to remove
	 */
	private void removeEventListener(IpReassemblyEventListener listener) {
		if (isDispatching) {
			if (pendingRemovals == null) {
				pendingRemovals = new ArrayList<>();
			}
			pendingRemovals.add(listener);
		} else {
			listeners.remove(listener);
		}
	}

	/**
	 * Dispatches an event to all registered listeners.
	 *
	 * @param event the event to dispatch
	 */
	public void dispatchEvent(IpReassemblyEvent event) {
		if (event == null || listeners.isEmpty()) {
			return;
		}

		isDispatching = true;
		try {
			for (IpReassemblyEventListener listener : listeners) {
				try {
					listener.onEvent(event);
				} catch (Exception e) {
					// Log exception but continue dispatching to other listeners
					handleListenerException(e, listener, event);
				}
			}
		} finally {
			isDispatching = false;
			processPendingModifications();
		}
	}

	/**
	 * Processes any pending listener modifications after event dispatch.
	 */
	private void processPendingModifications() {
		if (pendingRemovals != null) {
			pendingRemovals.forEach(listeners::remove);
			pendingRemovals = null;
		}

		if (pendingAdditions != null) {
			pendingAdditions.forEach(listeners::add);
			pendingAdditions = null;
		}
	}

	/**
	 * Handles exceptions thrown by event listeners.
	 */
	private void handleListenerException(Exception e, IpReassemblyEventListener listener, IpReassemblyEvent event) {
		System.err.printf("Error dispatching event %s to listener %s: %s%n",
				event.getType(), listener.getClass().getName(), e.getMessage());
	}

	/**
	 * Checks if there are any registered listeners.
	 *
	 * @return true if there are listeners, false otherwise
	 */
	public boolean hasListeners() {
		return !listeners.isEmpty();
	}

	/**
	 * Creates an empty event for testing or initialization purposes.
	 *
	 * @param type the type of event
	 * @return a new event instance
	 */
	public static IpReassemblyEvent createEmptyEvent(IpFragmentEventType type) {
		return new IpReassemblyEvent(type, null);
	}

	/**
	 * Removes all registered listeners.
	 */
	public void removeAllListeners() {
		if (isDispatching) {
			if (pendingRemovals == null) {
				pendingRemovals = new ArrayList<>(listeners);
			} else {
				pendingRemovals.addAll(listeners);
			}
		} else {
			listeners.clear();
		}
	}
}