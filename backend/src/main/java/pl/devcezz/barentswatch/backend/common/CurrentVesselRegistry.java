package pl.devcezz.barentswatch.backend.common;

import java.util.HashMap;
import java.util.Map;

public record CurrentVesselRegistry(Integer mmsi, String name, String shipType, String destination, PointInTime pointInTime) {

    public CurrentVesselRegistry(Integer mmsi, String name, String shipType, String destination, PointInTime pointInTime) {
        this.mmsi = mmsi;
        this.name = name;
        this.shipType = shipType;
        this.destination = destination;
        this.pointInTime = pointInTime;
    }

    public CurrentVesselRegistry(Integer mmsi, String name, Integer shipType, String destination, PointInTime pointInTime) {
        this(mmsi, name, SHIP_TYPES_MAP.getOrDefault(shipType, "Unknown"), destination, pointInTime);
    }

    private static final Map<Integer, String> SHIP_TYPES_MAP = new HashMap<>() {{
        put(20, "Wing in ground (WIG), all ships of this type");
        put(21, "Wing in ground (WIG), Hazardous category A");
        put(22, "Wing in ground (WIG), Hazardous category B");
        put(23, "Wing in ground (WIG), Hazardous category C");
        put(24, "Wing in ground (WIG), Hazardous category D");
        put(25, "Wing in ground (WIG), Reserved for future use");
        put(26, "Wing in ground (WIG), Reserved for future use");
        put(27, "Wing in ground (WIG), Reserved for future use");
        put(28, "Wing in ground (WIG), Reserved for future use");
        put(29, "Wing in ground (WIG), Reserved for future use");
        put(30, "Fishing");
        put(31, "Towing");
        put(32, "Towing: length exceeds 200m or breadth exceeds 25m");
        put(33, "Dredging or underwater ops");
        put(34, "Diving ops");
        put(35, "Military ops");
        put(36, "Sailing");
        put(37, "Pleasure Craft");
        put(38, "Reserved");
        put(39, "Reserved");
        put(40, "High speed craft (HSC), all ships of this type");
        put(41, "High speed craft (HSC), Hazardous category A");
        put(42, "High speed craft (HSC), Hazardous category B");
        put(43, "High speed craft (HSC), Hazardous category C");
        put(44, "High speed craft (HSC), Hazardous category D");
        put(45, "High speed craft (HSC), Reserved for future use");
        put(46, "High speed craft (HSC), Reserved for future use");
        put(47, "High speed craft (HSC), Reserved for future use");
        put(48, "High speed craft (HSC), Reserved for future use");
        put(49, "High speed craft (HSC), No additional information");
        put(50, "Pilot Vessel");
        put(51, "Search and Rescue vessel");
        put(52, "Tug");
        put(53, "Port Tender");
        put(54, "Anti-pollution equipment");
        put(55, "Law Enforcement");
        put(56, "Spare - Local Vessel");
        put(57, "Spare - Local Vessel");
        put(58, "Medical Transport");
        put(59, "Noncombatant ship according to RR Resolution No. 18");
        put(60, "Passenger, all ships of this type");
        put(61, "Passenger, Hazardous category A");
        put(62, "Passenger, Hazardous category B");
        put(63, "Passenger, Hazardous category C");
        put(64, "Passenger, Hazardous category D");
        put(65, "Passenger, Reserved for future use");
        put(66, "Passenger, Reserved for future use");
        put(67, "Passenger, Reserved for future use");
        put(68, "Passenger, Reserved for future use");
        put(69, "Passenger, No additional information");
        put(70, "Cargo, all ships of this type");
        put(71, "Cargo, Hazardous category A");
        put(72, "Cargo, Hazardous category B");
        put(73, "Cargo, Hazardous category C");
        put(74, "Cargo, Hazardous category D");
        put(75, "Cargo, Reserved for future use");
        put(76, "Cargo, Reserved for future use");
        put(77, "Cargo, Reserved for future use");
        put(78, "Cargo, Reserved for future use");
        put(79, "Cargo, No additional information");
        put(80, "Tanker, all ships of this type");
        put(81, "Tanker, Hazardous category A");
        put(82, "Tanker, Hazardous category B");
        put(83, "Tanker, Hazardous category C");
        put(84, "Tanker, Hazardous category D");
        put(85, "Tanker, Reserved for future use");
        put(86, "Tanker, Reserved for future use");
        put(87, "Tanker, Reserved for future use");
        put(88, "Tanker, Reserved for future use");
        put(89, "Tanker, No additional information");
        put(90, "Other Type, all ships of this type");
        put(91, "Other Type, Hazardous category A");
        put(92, "Other Type, Hazardous category B");
        put(93, "Other Type, Hazardous category C");
        put(94, "Other Type, Hazardous category D");
        put(95, "Other Type, Reserved for future use");
        put(96, "Other Type, Reserved for future use");
        put(97, "Other Type, Reserved for future use");
        put(98, "Other Type, Reserved for future use");
        put(99, "Other Type, no additional information");
    }};
}
