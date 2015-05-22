package org.mtransit.parser.ca_gtha_go_transit_bus;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MDirectionType;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MSpec;
import org.mtransit.parser.mt.data.MTrip;

// http://www.gotransit.com/publicroot/en/schedules/DeveloperResources.aspx
// http://www.gotransit.com/timetables/fr/schedules/DeveloperResources.aspx
// http://www.gotransit.com/publicroot/en/schedules/GTFSdownload.aspx
// http://www.gotransit.com/publicroot/gtfs/google_transit.zip
public class GTHAGOTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-gtha-go-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new GTHAGOTransitBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("Generating GO Transit bus data...\n");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("Generating GO Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	@Override
	public long getRouteId(GRoute gRoute) {
		Matcher matcher = DIGITS.matcher(gRoute.route_id);
		matcher.find();
		return Long.parseLong(matcher.group());
	}

	@Override
	public String getRouteShortName(GRoute gRoute) {
		Matcher matcher = DIGITS.matcher(gRoute.route_id);
		matcher.find();
		return matcher.group();
	}

	private static final String AGENCY_COLOR = "387C2B"; // GREEN (AGENCY WEB SITE CSS)

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public String getRouteColor(GRoute gRoute) {
		return null; // use agency color instead of provided colors (like web site)
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		int directionId = gTrip.direction_id;
		String stationName = cleanTripHeadsign(gTrip.trip_headsign);
		if (mRoute.id == 11l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			}
		} else if (mRoute.id == 12l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 15l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 16l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 18l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 19l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 20l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 21l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 25l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 27l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 29l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 30l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 31l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 32l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 33l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 34l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 35l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 36l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 37l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 38l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 39l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 40l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 45l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 46l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 47l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 48l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 51l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 52l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 54l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			}
		} else if (mRoute.id == 60l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 61l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 63l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 65l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 66l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 67l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 68l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 70l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 71l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 81l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 88l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.NORTH);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.SOUTH);
				return;
			}
		} else if (mRoute.id == 90l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			}
		} else if (mRoute.id == 91l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			}
		} else if (mRoute.id == 92l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			}
		} else if (mRoute.id == 93l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			}
		} else if (mRoute.id == 96l) {
			if (directionId == 0) {
				mTrip.setHeadsignDirection(MDirectionType.WEST);
				return;
			} else if (directionId == 1) {
				mTrip.setHeadsignDirection(MDirectionType.EAST);
				return;
			}
		} else {
			mTrip.setHeadsignString(stationName, directionId);
			System.out.println("Unexpected trip " + gTrip);
			System.exit(-1);
		}
	}

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		return MSpec.cleanLabel(tripHeadsign);
	}

	private static final Pattern AT = Pattern.compile("( at )", Pattern.CASE_INSENSITIVE);
	private static final String AT_REPLACEMENT = " / ";

	private static final Pattern GO = Pattern.compile("(^|\\s){1}(go)($|\\s){1}", Pattern.CASE_INSENSITIVE);
	private static final String GO_REPLACEMENT = " ";

	private static final Pattern VIA = Pattern.compile("(^|\\s){1}(via)($|\\s){1}", Pattern.CASE_INSENSITIVE);
	private static final String VIA_REPLACEMENT = " ";

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = AT.matcher(gStopName).replaceAll(AT_REPLACEMENT);
		gStopName = VIA.matcher(gStopName).replaceAll(VIA_REPLACEMENT);
		gStopName = GO.matcher(gStopName).replaceAll(GO_REPLACEMENT);
		gStopName = MSpec.cleanNumbers(gStopName);
		return MSpec.cleanLabel(gStopName);
	}
}
