package org.mtransit.parser.ca_gtha_go_transit_bus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;

// https://www.gotransit.com/en/information-resources/software-developers
// https://www.gotransit.com/fr/ressources-informatives/dveloppeurs-de-logiciel
// https://www.gotransit.com/static_files/gotransit/assets/Files/GO_GTFS.zip
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
		System.out.printf("\nGenerating GO Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this, true);
		super.start(args);
		System.out.printf("\nGenerating GO Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIds != null && this.serviceIds.isEmpty();
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

	@Override
	public long getRouteId(GRoute gRoute) {
		return Long.parseLong(gRoute.getRouteShortName()); // use route short name as route ID
	}

	private static final String AGENCY_COLOR = "387C2B"; // GREEN (AGENCY WEB SITE CSS)

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public String getRouteColor(GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor())) {
			int rsn = Integer.parseInt(gRoute.getRouteShortName());
			switch (rsn) {
			// @formatter:off
			case 11: return "98002e"; // St. Catharines / Niagara on the Lake
			case 70: return "794500"; //
			// @formatter:on
			}
			if (isGoodEnoughAccepted()) {
				return null;
			}
			System.out.printf("\nUnexpected route color for %s!\n", gRoute);
			System.exit(-1);
			return null;
		}
		return super.getRouteColor(gRoute);
	}

	private static final String _SLASH_ = " / ";
	private static final String UNIVERSITY_SHORT = "U";
	private static final String PARK_AND_RIDE_SHORT = "P&R";
	private static final String _PARK_AND_RIDE_SHORT = " " + PARK_AND_RIDE_SHORT;

	private static final String A_ = "A ";
	private static final String B_ = "B ";
	private static final String C_ = "C ";
	private static final String D_ = "D ";
	private static final String E_ = "E ";
	private static final String F_ = "F ";
	private static final String G_ = "G ";
	private static final String H_ = "H ";
	// I
	private static final String J_ = "J ";
	private static final String K_ = "K ";
	private static final String L_ = "L ";
	private static final String M_ = "M ";
	private static final String N_ = "N ";
	// O
	private static final String P_ = "P ";
	private static final String Q_ = "Q ";
	private static final String R_ = "R ";
	// S
	private static final String T_ = "T ";
	// U
	// V
	private static final String W_ = "W ";
	private static final String X_ = "X ";
	private static final String Y_ = "Y ";
	private static final String Z_ = "Z ";

	private static final String AJAX = "Ajax";
	private static final String ALDERSHOT = "Aldershot";
	private static final String ALLANDALE_WATERFRONT = "Allandale Waterfront";
	private static final String AURORA = "Aurora";
	private static final String BARRIE = "Barrie";
	private static final String BEAVERTON = "Beaverton";
	private static final String BOLTON = "Bolton";
	private static final String BOWMANVILLE = "Bowmanville";
	private static final String BOWMANVILLE_PARK_AND_RIDE = BOWMANVILLE + _PARK_AND_RIDE_SHORT;
	private static final String BRADFORD = "Bradford";
	private static final String BRAMALEA = "Bramalea";
	private static final String BRAMPTON = "Brampton";
	private static final String BRANTFORD = "Brantford";
	private static final String BRONTE_CARPOOL = "Bronte Carpool";
	private static final String BURLINGTON = "Burlington";
	private static final String BURLINGTON_CARPOOL = BURLINGTON + " Carpool";
	private static final String CENTENNIAL = "Centennial";
	private static final String CENTENNIAL_COLLEGE = CENTENNIAL + " College";
	private static final String CLARKSON = "Clarkson";
	private static final String COOKSVILLE = "Cooksville";
	private static final String DIXIE = "Dixie";
	private static final String DUNDAS = "Dundas";
	private static final String EAST_GWILLIMBURY = "East Gwillimbury";
	private static final String ERINDALE = "Erindale";
	private static final String ERIN_MILLS = "Erin Mills";
	private static final String FINCH = "Finch";
	private static final String GEORGETOWN = "Georgetown";
	private static final String GORMLEY = "Gormley";
	private static final String GUELPH = "Guelph";
	private static final String GUELPH_CENTRAL = GUELPH + " Central";
	private static final String HAMILTON = "Hamilton";
	private static final String HIGHWAY_SHORT = "Hwy";
	private static final String HIGHWAY_407 = HIGHWAY_SHORT + " 407";
	private static final String HIGHWAY_412 = HIGHWAY_SHORT + " 412";
	private static final String KING_CITY = "King City";
	private static final String LANGSTAFF = "Langstaff";
	private static final String LINCOLNVILLE = "Lincolnville";
	private static final String LISGAR = "Lisgar";
	private static final String MALTON = "Malton";
	private static final String MC_MASTER_UNIVERSITY = "McMaster " + UNIVERSITY_SHORT;
	private static final String MEADOWVALE = "Meadowvale";
	private static final String MILTON = "Milton";
	private static final String MOUNT_JOY = "Mt Joy";
	private static final String MOUNT_PLEASANT = "Mt Pleasant";
	private static final String NEWCASTLE = "Newcastle";
	private static final String NEWMARKET = "Newmarket";
	private static final String NIAGARA_FALLS = "Niagara Falls";
	private static final String OAKVILLE = "Oakville";
	private static final String OAKVILLE_CARPOOL = OAKVILLE + " Carpool";
	private static final String ORANGEVILLE = "Orangeville";
	private static final String ORANGEVILLE_MALL = ORANGEVILLE + " Mall";
	private static final String OSHAWA = "Oshawa";
	private static final String PEARSON_AIRPORT = "Pearson Airport";
	private static final String PICKERING = "Pickering";
	private static final String PORT_PERRY = "Port Perry";
	private static final String RICHMOND_HILL_CENTER = "Richmond Hl Ctr";
	private static final String RUTHERFORD = "Rutherford";
	private static final String SCARBORO = "Scarboro";
	private static final String SCARBOROUGH = "Scarborough";
	private static final String SHERIDAN_COLLEGE = "Sheridan College";
	private static final String SPECIAL = "Special";
	private static final String SQUARE_ONE = "Sq One";
	private static final String ST_CATHARINES = "St. Catharines";
	private static final String STREETSVILLE = "Streetsville";
	private static final String TRINITY_COMMON = "Trinity Common";
	private static final String UNIVERSITY_OF_GUELPH = UNIVERSITY_SHORT + " Of " + GUELPH;
	private static final String UNIVERSITY_OF_WATERLOO = UNIVERSITY_SHORT + " Of Waterloo";
	private static final String UNIVERSITY_OF_TORONTO_SCARBOROUGH = UNIVERSITY_SHORT + " Of T Scarboro";
	private static final String UNION = "Union";
	private static final String UNIONVILLE = "Unionville";
	private static final String UOIT_D_C = "UOIT / D.C.";
	private static final String UXBRIDGE = "Uxbridge";
	private static final String WEST_HARBOUR = "West Harbour";
	private static final String WESTON = "Weston";
	private static final String WHITBY = "Whitby";
	private static final String YORK_MILLS = "York Mills";
	private static final String YORK_U = "York " + UNIVERSITY_SHORT;
	private static final String YORKDALE = "Yorkdale";

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), gTrip.getDirectionId());
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		List<String> headsignsValues = Arrays.asList(mTrip.getHeadsignValue(), mTripToMerge.getHeadsignValue());
		if (mTrip.getRouteId() == 12L) {
			if (Arrays.asList( //
					A_ + BURLINGTON_CARPOOL, //
					B_ + BURLINGTON, //
					BURLINGTON_CARPOOL, //
					C_ + BURLINGTON, //
					D_ + BURLINGTON //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BURLINGTON_CARPOOL, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					B_ + NIAGARA_FALLS, //
					C_ + ST_CATHARINES, //
					NIAGARA_FALLS //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(NIAGARA_FALLS, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 15L) {
			if (Arrays.asList( //
					A_ + ALDERSHOT, //
					ALDERSHOT //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(ALDERSHOT, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + MC_MASTER_UNIVERSITY, //
					BRANTFORD //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BRANTFORD, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 18L) {
			if (Arrays.asList( //
					ALDERSHOT, //
					B_ + UNION, //
					G_ + UNION, //
					H_ + UNION, //
					UNION, // ++
					SPECIAL //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId()); // ALDERSHOT
				return true;
			} else if (Arrays.asList( //
					C_ + HAMILTON, //
					D_ + CLARKSON, //
					E_ + WEST_HARBOUR, //
					F_ + HAMILTON, //
					HAMILTON, //
					SPECIAL //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HAMILTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 19L) {
			if (Arrays.asList( //
					A_ + FINCH, //
					B_ + YORK_MILLS, //
					C_ + FINCH, //
					FINCH //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(FINCH, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + SQUARE_ONE, //
					B_ + SQUARE_ONE, //
					C_ + SQUARE_ONE, //
					SQUARE_ONE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(SQUARE_ONE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 21L) {
			if (Arrays.asList( //
					A_ + UNION, //
					B_ + UNION, //
					C_ + UNION, //
					D_ + UNION, //
					E_ + UNION, //
					F_ + COOKSVILLE, //
					G_ + UNION, //
					H_ + UNION, //
					J_ + UNION, //
					K_ + UNION, //
					L_ + UNION, //
					M_ + UNION, //
					N_ + UNION, //
					P_ + UNION, //
					R_ + UNION, //
					T_ + UNION, //
					UNION //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + MILTON, //
					B_ + MILTON, //
					D_ + LISGAR, //
					E_ + LISGAR, //
					G_ + ERINDALE, //
					F_ + SQUARE_ONE, //
					H_ + SQUARE_ONE, //
					J_ + SQUARE_ONE, //
					M_ + DIXIE, //
					N_ + MILTON, //
					P_ + MEADOWVALE, //
					MILTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MILTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 25L) {
			if (Arrays.asList( //
					B_ + SQUARE_ONE, //
					C_ + SQUARE_ONE, //
					D_ + SQUARE_ONE, //
					F_ + HIGHWAY_407, //
					F_ + YORK_U, //
					SQUARE_ONE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(SQUARE_ONE, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					C_ + UNIVERSITY_OF_WATERLOO, //
					F_ + UNIVERSITY_OF_WATERLOO, //
					UNIVERSITY_OF_WATERLOO //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNIVERSITY_OF_WATERLOO, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 27L) {
			if (Arrays.asList( //
					A_ + FINCH, //
					B_ + YORK_MILLS, //
					C_ + FINCH, //
					F_ + FINCH, //
					FINCH //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(FINCH, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + MILTON, //
					B_ + MEADOWVALE, //
					C_ + MILTON, //
					F_ + MEADOWVALE, //
					MILTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MILTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 29L) {
			if (Arrays.asList( //
					B_ + UNIVERSITY_OF_GUELPH, //
					GUELPH_CENTRAL //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(GUELPH_CENTRAL, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 31L) {
			if (Arrays.asList( //
					A_ + UNION, //
					D_ + BRAMALEA, //
					E_ + UNION, //
					F_ + UNION, //
					H_ + UNION, //
					J_ + UNION, //
					L_ + UNION, //
					UNION //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + UNIVERSITY_OF_GUELPH, //
					E_ + GEORGETOWN, //
					F_ + GEORGETOWN, //
					H_ + BRAMPTON, //
					L_ + BRAMALEA, //
					N_ + BRAMALEA, //
					UNIVERSITY_OF_GUELPH //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNIVERSITY_OF_GUELPH, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 32L) {
			if (Arrays.asList( //
					A_ + YORK_MILLS, //
					B_ + YORK_MILLS, //
					YORK_MILLS //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_MILLS, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + BRAMALEA, //
					B_ + BRAMALEA, //
					TRINITY_COMMON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(TRINITY_COMMON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 33L) {
			if (Arrays.asList( //
					A_ + YORK_MILLS, //
					C_ + GEORGETOWN, //
					D_ + BRAMPTON, //
					E_ + YORK_MILLS, //
					YORK_MILLS //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_MILLS, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + BRAMPTON, //
					B_ + MOUNT_PLEASANT, //
					C_ + UNIVERSITY_OF_GUELPH, //
					D_ + UNIVERSITY_OF_GUELPH, //
					E_ + GEORGETOWN, //
					F_ + UNIVERSITY_OF_GUELPH, //
					G_ + GEORGETOWN, //
					UNIVERSITY_OF_GUELPH //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNIVERSITY_OF_GUELPH, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 35L) {
			if (Arrays.asList( //
					A_ + PEARSON_AIRPORT + " UP Express", // ==
					WESTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(WESTON, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + PEARSON_AIRPORT + " UP Express", // ==
					PEARSON_AIRPORT + " UP Express" //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(PEARSON_AIRPORT + " UP Express", mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 36L) {
			if (Arrays.asList( //
					B_ + YORK_MILLS, //
					YORK_MILLS //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_MILLS, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					B_ + BRAMALEA, //
					BRAMPTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BRAMPTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 37L) {
			if (Arrays.asList( //
					B_ + BRAMPTON, //
					BRAMPTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BRAMPTON, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + ORANGEVILLE_MALL, //
					ORANGEVILLE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(ORANGEVILLE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 38L) {
			if (Arrays.asList( //
					A_ + BOLTON, //
					BOLTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BOLTON, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + YORK_MILLS, //
					MALTON, //
					YORK_MILLS // ++
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_MILLS, mTrip.getHeadsignId()); // MALTON
				return true;
			}
		} else if (mTrip.getRouteId() == 40L) {
			if (Arrays.asList( //
					A_ + SQUARE_ONE, //
					RICHMOND_HILL_CENTER //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(RICHMOND_HILL_CENTER, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + HAMILTON, //
					B_ + PEARSON_AIRPORT, // .
					HAMILTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HAMILTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 45L) {
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					HIGHWAY_407, //
					A_ + YORK_U, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + SQUARE_ONE, //
					STREETSVILLE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(STREETSVILLE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 46L) {
			if (Arrays.asList( //
					A_ + SQUARE_ONE, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + SQUARE_ONE, //
					HIGHWAY_407, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + SHERIDAN_COLLEGE, //
					OAKVILLE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(OAKVILLE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 47L) {
			if (Arrays.asList( //
					A_ + BRAMALEA, //
					B_ + SQUARE_ONE, //
					C_ + ERIN_MILLS, //
					E_ + OAKVILLE_CARPOOL, //
					F_ + HIGHWAY_407, //
					H_ + BRONTE_CARPOOL, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + BRAMALEA, //
					B_ + SQUARE_ONE, //
					C_ + ERIN_MILLS, //
					E_ + OAKVILLE_CARPOOL, //
					F_ + HIGHWAY_407, //
					H_ + BRONTE_CARPOOL, //
					HIGHWAY_407, //
					F_ + YORK_U, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					B_ + MC_MASTER_UNIVERSITY, //
					C_ + MC_MASTER_UNIVERSITY, //
					F_ + MC_MASTER_UNIVERSITY, //
					H_ + MC_MASTER_UNIVERSITY, //
					HAMILTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HAMILTON, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 48L) {
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					B_ + HIGHWAY_407, //
					F_ + HIGHWAY_407, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					B_ + HIGHWAY_407, //
					F_ + HIGHWAY_407, //
					HIGHWAY_407, //
					A_ + YORK_U, //
					B_ + YORK_U, //
					F_ + YORK_U, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + MEADOWVALE, //
					B_ + MEADOWVALE, //
					F_ + UNIVERSITY_OF_GUELPH, //
					UNIVERSITY_OF_GUELPH //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNIVERSITY_OF_GUELPH, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 51L) {
			if (Arrays.asList( //
					A_ + SCARBOROUGH, //
					B_ + PICKERING, //
					C_ + UNIVERSITY_OF_TORONTO_SCARBOROUGH, //
					D_ + UNIVERSITY_OF_TORONTO_SCARBOROUGH, //
					D_ + CENTENNIAL_COLLEGE, //
					PICKERING //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(PICKERING, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					B_ + HIGHWAY_407, //
					C_ + HIGHWAY_407, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					B_ + HIGHWAY_407, //
					C_ + HIGHWAY_407, //
					HIGHWAY_407, //
					A_ + YORK_U, //
					B_ + YORK_U, //
					C_ + YORK_U, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 52L) {
			if (Arrays.asList( //
					B_ + HIGHWAY_407, //
					D_ + HIGHWAY_407, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					B_ + HIGHWAY_407, //
					D_ + HIGHWAY_407, //
					HIGHWAY_407, //
					B_ + YORK_U, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					A_ + UOIT_D_C, //
					D_ + UNIONVILLE, //
					OSHAWA //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(OSHAWA, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 54L) {
			if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					B_ + HIGHWAY_407, //
					C_ + HIGHWAY_407, //
					HIGHWAY_407 //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(HIGHWAY_407, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + HIGHWAY_407, //
					B_ + HIGHWAY_407, //
					C_ + HIGHWAY_407, //
					HIGHWAY_407, //
					A_ + YORK_U, //
					B_ + YORK_U, //
					C_ + YORK_U, //
					YORK_U //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORK_U, mTrip.getHeadsignId());
				return true;
			}
			if (Arrays.asList( //
					C_ + MOUNT_JOY, //
					MOUNT_JOY //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MOUNT_JOY, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 61L) {
			if (Arrays.asList( //
					B_ + LANGSTAFF, //
					C_ + GORMLEY, //
					E_ + GORMLEY, //
					GORMLEY //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(GORMLEY, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + UNION, //
					B_ + UNION, //
					C_ + UNION, //
					D_ + UNION, //
					F_ + UNION, //
					UNION //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 63L) {
			if (Arrays.asList( //
					A_ + KING_CITY, //
					Y_ + AURORA, //
					KING_CITY //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(KING_CITY, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + UNION, //
					W_ + UNION, //
					Y_ + RUTHERFORD,//
					UNION //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 65L) {
			if (Arrays.asList( //
					A_ + NEWMARKET, //
					B_ + EAST_GWILLIMBURY, //
					C_ + AURORA, //
					G_ + EAST_GWILLIMBURY, //
					R_ + EAST_GWILLIMBURY, //
					T_ + EAST_GWILLIMBURY, //
					W_ + EAST_GWILLIMBURY, //
					X_ + AURORA, //
					EAST_GWILLIMBURY //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(EAST_GWILLIMBURY, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					B_ + UNION, //
					C_ + UNION, //
					D_ + UNION, //
					E_ + UNION, //
					F_ + UNION, //
					G_ + UNION, //
					R_ + AURORA, //
					T_ + RUTHERFORD, //
					X_ + RUTHERFORD, //
					Y_ + UNION, //
					UNION //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 66L) {
			if (Arrays.asList( //
					A_ + NEWMARKET, //
					NEWMARKET //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(NEWMARKET, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + EAST_GWILLIMBURY, //
					A_ + NEWMARKET, //
					EAST_GWILLIMBURY, //
					NEWMARKET //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(EAST_GWILLIMBURY, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + YORKDALE, //
					YORKDALE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORKDALE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 68L) {
			if (Arrays.asList( //
					A_ + BRADFORD, //
					B_ + BARRIE, //
					C_ + BARRIE, //
					D_ + ALLANDALE_WATERFRONT, //
					X_ + ALLANDALE_WATERFRONT, //
					BARRIE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BARRIE, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					AURORA, //
					B_ + EAST_GWILLIMBURY, //
					C_ + AURORA, //
					D_ + UNION, //
					W_ + RUTHERFORD, //
					Y_ + KING_CITY //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(AURORA, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 69L) {
			if (Arrays.asList( //
					A_ + AURORA + _SLASH_ + "404", //
					NEWMARKET //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(NEWMARKET, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + AURORA, //
					AURORA //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(AURORA, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 70L) {
			if (Arrays.asList( //
					B_ + UXBRIDGE, //
					C_ + LINCOLNVILLE, //
					D_ + UXBRIDGE, //
					E_ + MOUNT_JOY, //
					F_ + LINCOLNVILLE, //
					UXBRIDGE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UXBRIDGE, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + UNIONVILLE, //
					B_ + LINCOLNVILLE, //
					D_ + MOUNT_JOY, //
					E_ + UNIONVILLE, //
					UNIONVILLE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNIONVILLE, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 71L) {
			if (Arrays.asList( //
					A_ + UXBRIDGE, //
					C_ + LINCOLNVILLE, //
					D_ + LINCOLNVILLE, //
					F_ + CENTENNIAL, //
					G_ + UNIONVILLE, //
					UXBRIDGE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UXBRIDGE, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + UNION, //
					C_ + UNION, //
					D_ + UNION, //
					E_ + UNION, //
					F_ + UNION, //
					G_ + UNION, //
					UNION //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 81L) {
			if (Arrays.asList( //
					A_ + PORT_PERRY, //
					BEAVERTON //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(BEAVERTON, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + WHITBY, //
					WHITBY //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(WHITBY, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 90L) {
			if (Arrays.asList( //
					A_ + BOWMANVILLE, //
					A_ + NEWCASTLE, //
					B_ + OSHAWA, //
					C_ + NEWCASTLE, //
					D_ + BOWMANVILLE_PARK_AND_RIDE, //
					P_ + OSHAWA, //
					Q_ + OSHAWA, //
					R_ + OSHAWA, //
					T_ + WHITBY, //
					W_ + OSHAWA, //
					X_ + WHITBY, //
					Y_ + AJAX, //
					Z_ + OSHAWA, //
					AJAX, //
					OSHAWA, //
					SPECIAL, //
					WHITBY, //
					NEWCASTLE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(NEWCASTLE, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + OSHAWA, //
					B_ + UNION, //
					P_ + WHITBY, //
					Q_ + AJAX, //
					Q_ + OSHAWA, //
					R_ + AJAX, //
					T_ + AJAX, //
					W_ + PICKERING, //
					X_ + PICKERING, //
					Y_ + PICKERING, //
					Z_ + PICKERING, //
					OSHAWA, //
					PICKERING, //
					SPECIAL, //
					UNION // ++
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(UNION, mTrip.getHeadsignId()); // OSHAWA
				return true;
			}
		} else if (mTrip.getRouteId() == 92L) {
			if (Arrays.asList( //
					A_ + AJAX, //
					A_ + DUNDAS + _SLASH_ + HIGHWAY_412 + _PARK_AND_RIDE_SHORT, //
					OSHAWA //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(OSHAWA, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + FINCH, //
					A_ + YORKDALE, //
					A_ + DUNDAS + _SLASH_ + HIGHWAY_412 + _PARK_AND_RIDE_SHORT, //
					YORKDALE //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(YORKDALE, mTrip.getHeadsignId()); // OSHAWA
				return true;
			}
		} else if (mTrip.getRouteId() == 96L) {
			if (Arrays.asList( //
					A_ + OSHAWA, //
					B_ + OSHAWA, //
					C_ + AJAX, //
					D_ + OSHAWA, //
					E_ + SCARBORO, //
					SPECIAL, //
					OSHAWA //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(OSHAWA, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					A_ + SCARBORO, //
					B_ + FINCH, //
					C_ + FINCH, //
					D_ + FINCH, //
					E_ + FINCH, //
					SPECIAL, //
					FINCH //
					).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(FINCH, mTrip.getHeadsignId()); // OSHAWA
				return true;
			}
		}
		System.out.printf("\n%s: Unexpected trips to merge: %s & %s!\n", mTrip.getRouteId(), mTrip, mTripToMerge);
		System.exit(-1);
		return false;
	}

	private static final Pattern STARTS_WITH_RSN = Pattern.compile("(^[0-9]{2,3}([A-Z]?)(\\s+)\\- )", Pattern.CASE_INSENSITIVE);
	private static final String STARTS_WITH_RSN_REPLACEMENT = "$2$3";

	private static final Pattern STATION = Pattern.compile("((^|\\W){1}(station|sta|stn)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String STATION_REPLACEMENT = "$2$4";

	private static final Pattern PARK_AND_RIDE = Pattern.compile("((^|\\W){1}(park & ride|P\\+R)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String PARK_AND_RIDE_REPLACEMENT = "$2" + PARK_AND_RIDE_SHORT + "$4";

	private static final Pattern UNIVERSITY = Pattern.compile("((^|\\W){1}(university)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String UNIVERSITY_REPLACEMENT = "$2" + UNIVERSITY_SHORT + "$4";

	private static final Pattern HIGHWAY_ = Pattern.compile("((^|\\W){1}(highway|hwy\\.)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String HIGHWAY_REPLACEMENT = "$2" + HIGHWAY_SHORT + "$4";

	private static final Pattern CLEAN_DASH = Pattern.compile("(^[\\s]*\\-[\\s]*|[\\s]*\\-[\\s]*$)", Pattern.CASE_INSENSITIVE);

	private static final Pattern BUS_TERMINAL = Pattern.compile("( bus loop| bus terminal| bus term[\\.]?| terminal| term[\\.]?)", Pattern.CASE_INSENSITIVE);

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = STARTS_WITH_RSN.matcher(tripHeadsign).replaceAll(STARTS_WITH_RSN_REPLACEMENT);
		tripHeadsign = GO.matcher(tripHeadsign).replaceAll(GO_REPLACEMENT);
		tripHeadsign = STATION.matcher(tripHeadsign).replaceAll(STATION_REPLACEMENT);
		tripHeadsign = PARK_AND_RIDE.matcher(tripHeadsign).replaceAll(PARK_AND_RIDE_REPLACEMENT);
		tripHeadsign = UNIVERSITY.matcher(tripHeadsign).replaceAll(UNIVERSITY_REPLACEMENT);
		tripHeadsign = HIGHWAY_.matcher(tripHeadsign).replaceAll(HIGHWAY_REPLACEMENT);
		tripHeadsign = BUS_TERMINAL.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		tripHeadsign = CLEAN_DASH.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		int indexOfDash = tripHeadsign.indexOf("- ");
		if (indexOfDash >= 0) {
			tripHeadsign = tripHeadsign.substring(0, indexOfDash);
		}
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		if (StringUtils.isAllUpperCase(tripHeadsign)) {
			tripHeadsign = tripHeadsign.toLowerCase(Locale.ENGLISH);
		}
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	private static final Pattern GO = Pattern.compile("(^|\\s){1}(go)($|\\s){1}", Pattern.CASE_INSENSITIVE);
	private static final String GO_REPLACEMENT = " ";

	private static final Pattern VIA = Pattern.compile("(^|\\s){1}(via)($|\\s){1}", Pattern.CASE_INSENSITIVE);
	private static final String VIA_REPLACEMENT = " ";

	private static final Pattern POINT = Pattern.compile("((^|\\W){1}(st|ave|blvd|hwy|rd|dr)\\.(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String POINT_REPLACEMENT = "$2$3$4";

	private static final Pattern DIRECTION = Pattern.compile("((^|\\W){1}(s|n|e|w)\\.(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String DIRECTION_REPLACEMENT = "$2$3$4";

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = VIA.matcher(gStopName).replaceAll(VIA_REPLACEMENT);
		gStopName = GO.matcher(gStopName).replaceAll(GO_REPLACEMENT);
		gStopName = POINT.matcher(gStopName).replaceAll(POINT_REPLACEMENT);
		gStopName = DIRECTION.matcher(gStopName).replaceAll(DIRECTION_REPLACEMENT);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	private static final String SID_UN = "UN";
	private static final int UN_SID = 9021;
	private static final String SID_EX = "EX";
	private static final int EX_SID = 9022;
	private static final String SID_MI = "MI";
	private static final int MI_SID = 9031;
	private static final String SID_LO = "LO";
	private static final int LO_SID = 9033;
	private static final String SID_DA = "DA";
	private static final int DA_SID = 9061;
	private static final String SID_SC = "SC";
	private static final int SC_SID = 9062;
	private static final String SID_EG = "EG";
	private static final int EG_SID = 9063;
	private static final String SID_GU = "GU";
	private static final int GU_SID = 9081;
	private static final String SID_RO = "RO";
	private static final int RO_SID = 9091;
	private static final String SID_PO = "PO";
	private static final int PO_SID = 9111;
	private static final String SID_CL = "CL";
	private static final int CL_SID = 9121;
	private static final String SID_OA = "OA";
	private static final int OA_SID = 9131;
	private static final String SID_BO = "BO";
	private static final int BO_SID = 9141;
	private static final String SID_AP = "AP";
	private static final int AP_SID = 9151;
	private static final String SID_BU = "BU";
	private static final int BU_SID = 9161;
	private static final String SID_AL = "AL";
	private static final int AL_SID = 9171;
	private static final String SID_PIN = "PIN";
	private static final int PIN_SID = 9911;
	private static final String SID_AJ = "AJ";
	private static final int AJ_SID = 9921;
	private static final String SID_WH = "WH";
	private static final int WH_SID = 9939;
	private static final String SID_OS = "OS";
	private static final int OS_SID = 9941;
	private static final String SID_BL = "BL";
	private static final int BL_SID = 9023;
	private static final String SID_KP = "KP";
	private static final int KP_SID = 9032;
	private static final String SID_WE = "WE";
	private static final int WE_SID = 9041;
	private static final String SID_ET = "ET";
	private static final int ET_SID = 9042;
	private static final String SID_OR = "OR";
	private static final int OR_SID = 9051;
	private static final String SID_OL = "OL";
	private static final int OL_SID = 9052;
	private static final String SID_AG = "AG";
	private static final int AG_SID = 9071;
	private static final String SID_DI = "DI";
	private static final int DI_SID = 9113;
	private static final String SID_CO = "CO";
	private static final int CO_SID = 9114;
	private static final String SID_ER = "ER";
	private static final int ER_SID = 9123;
	private static final String SID_HA = "HA";
	private static final int HA_SID = 9181;
	private static final String SID_YO = "YO";
	private static final int YO_SID = 9191;
	private static final String SID_SR = "SR";
	private static final int SR_SID = 9211;
	private static final String SID_ME = "ME";
	private static final int ME_SID = 9221;
	private static final String SID_LS = "LS";
	private static final int LS_SID = 9231;
	private static final String SID_ML = "ML";
	private static final int ML_SID = 9241;
	private static final String SID_KI = "KI";
	private static final int KI_SID = 9271;
	private static final String SID_MA = "MA";
	private static final int MA_SID = 9311;
	private static final String SID_BE = "BE";
	private static final int BE_SID = 9321;
	private static final String SID_BR = "BR";
	private static final int BR_SID = 9331;
	private static final String SID_MO = "MO";
	private static final int MO_SID = 9341;
	private static final String SID_GE = "GE";
	private static final int GE_SID = 9351;
	private static final String SID_AC = "AC";
	private static final int AC_SID = 9371;
	private static final String SID_GL = "GL";
	private static final int GL_SID = 9391;
	private static final String SID_EA = "EA";
	private static final int EA_SID = 9441;
	private static final String SID_LA = "LA";
	private static final int LA_SID = 9601;
	private static final String SID_RI = "RI";
	private static final int RI_SID = 9612;
	private static final String SID_MP = "MP";
	private static final int MP_SID = 9613;
	private static final String SID_RU = "RU";
	private static final int RU_SID = 9614;
	private static final String SID_KC = "KC";
	private static final int KC_SID = 9621;
	private static final String SID_AU = "AU";
	private static final int AU_SID = 9631;
	private static final String SID_NE = "NE";
	private static final int NE_SID = 9641;
	private static final String SID_BD = "BD";
	private static final int BD_SID = 9651;
	private static final String SID_BA = "BA";
	private static final int BA_SID = 9681;
	private static final String SID_AD = "AD";
	private static final int AD_SID = 9691;
	private static final String SID_MK = "MK";
	private static final int MK_SID = 9701;
	private static final String SID_UI = "UI";
	private static final int UI_SID = 9712;
	private static final String SID_MR = "MR";
	private static final int MR_SID = 9721;
	private static final String SID_CE = "CE";
	private static final int CE_SID = 9722;
	private static final String SID_MJ = "MJ";
	private static final int MJ_SID = 9731;
	private static final String SID_ST = "ST";
	private static final int ST_SID = 9741;
	private static final String SID_LI = "LI";
	private static final int LI_SID = 9742;
	private static final String SID_KE = "KE";
	private static final int KE_SID = 9771;
	private static final String SID_JAMES_STR = "JAMES STR";
	private static final int JAMES_STR_SID = 100001;
	private static final String SID_USBT = "USBT";
	private static final int USBT_SID = 52;
	private static final String SID_NI = "NI";
	private static final int NI_SID = 100003;
	private static final String SID_PA = "PA";
	private static final int PA_SID = 311;
	private static final String SID_SCTH = "SCTH";
	private static final int SCTH_SID = 100005;
	private static final String SID_DW = "DW";
	private static final int DW_SID = 100006;

	@Override
	public int getStopId(GStop gStop) {
		if (!Utils.isDigitsOnly(gStop.getStopId())) {
			if (SID_UN.equals(gStop.getStopId())) {
				return UN_SID;
			} else if (SID_EX.equals(gStop.getStopId())) {
				return EX_SID;
			} else if (SID_MI.equals(gStop.getStopId())) {
				return MI_SID;
			} else if (SID_LO.equals(gStop.getStopId())) {
				return LO_SID;
			} else if (SID_DA.equals(gStop.getStopId())) {
				return DA_SID;
			} else if (SID_SC.equals(gStop.getStopId())) {
				return SC_SID;
			} else if (SID_EG.equals(gStop.getStopId())) {
				return EG_SID;
			} else if (SID_GU.equals(gStop.getStopId())) {
				return GU_SID;
			} else if (SID_RO.equals(gStop.getStopId())) {
				return RO_SID;
			} else if (SID_PO.equals(gStop.getStopId())) {
				return PO_SID;
			} else if (SID_CL.equals(gStop.getStopId())) {
				return CL_SID;
			} else if (SID_OA.equals(gStop.getStopId())) {
				return OA_SID;
			} else if (SID_BO.equals(gStop.getStopId())) {
				return BO_SID;
			} else if (SID_AP.equals(gStop.getStopId())) {
				return AP_SID;
			} else if (SID_BU.equals(gStop.getStopId())) {
				return BU_SID;
			} else if (SID_AL.equals(gStop.getStopId())) {
				return AL_SID;
			} else if (SID_PIN.equals(gStop.getStopId())) {
				return PIN_SID;
			} else if (SID_AJ.equals(gStop.getStopId())) {
				return AJ_SID;
			} else if (SID_WH.equals(gStop.getStopId())) {
				return WH_SID;
			} else if (SID_OS.equals(gStop.getStopId())) {
				return OS_SID;
			} else if (SID_BL.equals(gStop.getStopId())) {
				return BL_SID;
			} else if (SID_KP.equals(gStop.getStopId())) {
				return KP_SID;
			} else if (SID_WE.equals(gStop.getStopId())) {
				return WE_SID;
			} else if (SID_ET.equals(gStop.getStopId())) {
				return ET_SID;
			} else if (SID_OR.equals(gStop.getStopId())) {
				return OR_SID;
			} else if (SID_OL.equals(gStop.getStopId())) {
				return OL_SID;
			} else if (SID_AG.equals(gStop.getStopId())) {
				return AG_SID;
			} else if (SID_DI.equals(gStop.getStopId())) {
				return DI_SID;
			} else if (SID_CO.equals(gStop.getStopId())) {
				return CO_SID;
			} else if (SID_ER.equals(gStop.getStopId())) {
				return ER_SID;
			} else if (SID_HA.equals(gStop.getStopId())) {
				return HA_SID;
			} else if (SID_YO.equals(gStop.getStopId())) {
				return YO_SID;
			} else if (SID_SR.equals(gStop.getStopId())) {
				return SR_SID;
			} else if (SID_ME.equals(gStop.getStopId())) {
				return ME_SID;
			} else if (SID_LS.equals(gStop.getStopId())) {
				return LS_SID;
			} else if (SID_ML.equals(gStop.getStopId())) {
				return ML_SID;
			} else if (SID_KI.equals(gStop.getStopId())) {
				return KI_SID;
			} else if (SID_MA.equals(gStop.getStopId())) {
				return MA_SID;
			} else if (SID_BE.equals(gStop.getStopId())) {
				return BE_SID;
			} else if (SID_BR.equals(gStop.getStopId())) {
				return BR_SID;
			} else if (SID_MO.equals(gStop.getStopId())) {
				return MO_SID;
			} else if (SID_GE.equals(gStop.getStopId())) {
				return GE_SID;
			} else if (SID_AC.equals(gStop.getStopId())) {
				return AC_SID;
			} else if (SID_GL.equals(gStop.getStopId())) {
				return GL_SID;
			} else if (SID_EA.equals(gStop.getStopId())) {
				return EA_SID;
			} else if (SID_LA.equals(gStop.getStopId())) {
				return LA_SID;
			} else if (SID_RI.equals(gStop.getStopId())) {
				return RI_SID;
			} else if (SID_MP.equals(gStop.getStopId())) {
				return MP_SID;
			} else if (SID_RU.equals(gStop.getStopId())) {
				return RU_SID;
			} else if (SID_KC.equals(gStop.getStopId())) {
				return KC_SID;
			} else if (SID_AU.equals(gStop.getStopId())) {
				return AU_SID;
			} else if (SID_NE.equals(gStop.getStopId())) {
				return NE_SID;
			} else if (SID_BD.equals(gStop.getStopId())) {
				return BD_SID;
			} else if (SID_BA.equals(gStop.getStopId())) {
				return BA_SID;
			} else if (SID_AD.equals(gStop.getStopId())) {
				return AD_SID;
			} else if (SID_MK.equals(gStop.getStopId())) {
				return MK_SID;
			} else if (SID_UI.equals(gStop.getStopId())) {
				return UI_SID;
			} else if (SID_MR.equals(gStop.getStopId())) {
				return MR_SID;
			} else if (SID_CE.equals(gStop.getStopId())) {
				return CE_SID;
			} else if (SID_MJ.equals(gStop.getStopId())) {
				return MJ_SID;
			} else if (SID_ST.equals(gStop.getStopId())) {
				return ST_SID;
			} else if (SID_LI.equals(gStop.getStopId())) {
				return LI_SID;
			} else if (SID_KE.equals(gStop.getStopId())) {
				return KE_SID;
			} else if (SID_JAMES_STR.equals(gStop.getStopId())) {
				return JAMES_STR_SID;
			} else if (SID_USBT.equals(gStop.getStopId())) {
				return USBT_SID;
			} else if (SID_NI.equals(gStop.getStopId())) {
				return NI_SID;
			} else if (SID_PA.equals(gStop.getStopId())) {
				return PA_SID;
			} else if (SID_SCTH.equals(gStop.getStopId())) {
				return SCTH_SID;
			} else if (SID_DW.equals(gStop.getStopId())) {
				return DW_SID;
			} else {
				System.out.println("Unexpected stop ID for " + gStop + "! (" + gStop.getStopId() + ")");
				System.exit(-1);
				return -1;
			}
		}
		return super.getStopId(gStop);
	}
}
