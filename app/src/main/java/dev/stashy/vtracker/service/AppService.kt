package dev.stashy.vtracker.service

import dev.stashy.vtracker.service.camera.CameraService

interface AppService : TrackerService, CameraService
